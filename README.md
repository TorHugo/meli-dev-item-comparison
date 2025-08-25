# Meli - Item Comparison
### Objetivo
- Construir uma API backend simplificada que forneça detalhes de produtos para uso em uma funcionalidade de comparação de itens. Sua implementação deve seguir boas práticas estabelecidas de backend, fornecendo endpoints claros e eficientes para recuperar os dados necessários para comparações de produtos.

### Requisitos
- Construa uma API RESTFull básica que retorne detalhes de múltiplos itens para serem comparados.
- A API também deve fornecer campos como: nome do produto, URL da imagem, descrição, preço, avaliação e especificações.
- Inclua tratamento básico de erros e comentários inline para explicar sua lógica.

### Tecnologias Utilizadas
**Kotlin** ``v1.9.25`` <br>
**SpringBoot** ``v3.5.5``<br>

### Estrutura do Projeto
```cmd
.
├── .gitignore
├── build.gradle
├── gradlew
├── gradlew.bat
├── README.md
├── settings.gradle
└── src
    └── main
        ├── kotlin
        │   └── dev
        │       └── com
        │           └── item
        │               └── compartison
        │                   ├── application
        │                   │   ├── helper
        │                   │   ├── usecase
        │                   │   │   ├── context
        │                   │   │   └── strategy
        │                   │   └── utils
        │                   ├── domain
        │                   │   ├── entity
        │                   │   ├── enums
        │                   │   ├── exception
        │                   │   │   └── template
        │                   │   ├── gateway
        │                   │   ├── helper
        │                   │   ├── objects
        │                   │   ├── service
        │                   │   └── utils
        │                   └── infrastructure
        │                       ├── adapter
        │                       ├── api
        │                       │   ├── mapper
        │                       │   └── models
        │                       │       ├── request
        │                       │       └── response
        │                       ├── config
        │                       ├── exception
        │                       ├── gateway
        │                       ├── service
        │                       └── utils
        └── resources
            └── data
```

### Documentação & visão estratégica

#### Obs.: para utilização dos endpoints, basta acessar o swagger da aplicação em: ``http://localhost:8080/api/swagger-ui/index.html#/``.


#### Design e endpoints principais da API
- Para a implementação desse desafio, decidi utilizar uma abstração do `Domain Driven Design` que trás diversos benefícios para o desenvolvimento. Desde manutenabilidade, testabilidade e clareza na separação de responsabilidades, até a possibilidade de evoluir o sistema de forma consistente à medida que as regras de negócio se tornam mais complexas.
- Em relação aos endpoints principais, posso citar três: 
  - Busca de todos os produtos páginada.
    ````http request
    GET /api/v1/products/find-all
    ````  
    - Busca de produto específica por identificador.
    ````http request
    GET /api/v1/products/{productId}
    ````
    - Comparação dos produtos com base em critérios solicitados.
    ````http request
      GET /api/v1/products/comparison
    ````

- Os dois primeiros, não temos muito segredo. Simulamos a recuperação de alguns produtos do banco de dados, porém, na verdade são recuperados de um arquivo ``.json`` como solicitado. A ideia é que o primeiro traga todos os produtos "cadastrados" no datasource, e o segundo, traga um produto exclusivo com base no seu identificador no datasource.
- Em relação ao terceiro endpoint, que seria o ``core`` da aplicação tem alguns fatores que irei dar uma enfase maior:
  - Com base nos headers e path params enviados, o fluxo principal irá utilizar desses parâmetros para entender qual estratégia de ordenação utilizar. Atualmente temos três estratégias - do contexto de ``strategy pattern`` - implementadas no projeto:
    - ````PriceComparisonStrategy````, que permite a ordenação dos produtos por preço;
    - ````RatingComparisonStrategy````, que possibilita a ordenação dos produtos por avaliação;
    - ````SpecificationComparisonStrategy````, e por fim, a ordenação dos produtos com base em alguma especificação técnica do produto.
  - Para distinguir qual estratégia utilizar, enviamos um parâmetro no headers da chamada - que inclusive, é obrigatório - ````type````, parâmetros possíveis: _PRICE_, _RATING_ e _SPECIFICATION_.
  - Também via headers, enviamos outro parâmetro indicando a direção da ordenação - ````higherIsBetter```` - que seria no caso um boolean. Quando enviamos como "true", queremos ordenar os produtos do maior para o menor. Ex.: no contexto de preço, do mais caro para o mais barato.
  - Quando queremos utilizar a ordenação por alguma especificação técnica, devemos apontar qual atributo queremos validar. E para isso, é obrigatório o envio do parâmetro ````specificationKey```` - via headers da chamada.

    | Headers          | Tipo    | Obrigatório | Descrição                                                 |
    |------------------|---------|-------------|-----------------------------------------------------------|
    | type             | string  | Sim         | Tipo da comparação (`PRICE`, `RATING` ou `SPECIFICATION`) |
    | specificationKey | string  | Não         | Especificação técnica  (ex.: `armazenamento`)             |
    | higherIsBetter   | boolean | Sim         | Direção da comparação (ex.: `true`)                       |

  - Vale ressaltar que para a consulta dos produtos, é realizado uma pré-filtragem com base em alguns parâmetros que podem ser enviados:
    
      | Request Params | Tipo   | Obrigatório | Descrição                                 |
      |----------------|--------|-------------|-------------------------------------------|
      | category       | string | Sim         | Categoria dos produtos (ex.: `notebooks`) |
      | brand          | string | Não         | Marca dos produtos (ex.: `Dell`)          |
      | name           | string | Não         | Nome do produto (ex.: `MacBook Air M3`)   |
      | discount       | double | Não         | Desconto oferecido (ex.: `20.0`)          |
      | minPrice       | string | Não         | Menor preço (ex.: `3.000`)                |
      | maxPrice       | string | Não         | Maior preço (ex.: `10.000`)               |
    
  - Quando qualquer um desses parâmetros são enviados automaticamente é realizado uma pré-filtragem nos produtos que possuírem os parâmetros requisitados.

### Decisões arquiteturais importantes tomadas durante o desenvolvimento
- Em relação a esse tema, gostaria de ressaltar duas decisões que tive que tomar:
  1. Como segregar dos tipos de ordenação, de maneira que fosse fácil de dar manutenção e adepta ao crescimento exponencial que pode ter.
  2. Como recuperar os produtos do datasource, de maneira dinâmica e sem aumentar muito o custo.

- E em relação a esses "questionamentos", resolvi seguir da seguinte maneira:
  - Para a segregação dos tipos de ordenação, utilizei um ````Design Pattern (Strategy Pattern)```` que me possíbilita criar diversas estratégias para uma mesma tarefa e tornar esses algoritmos intercambiáveis em tempo de execução. Evitando condicionais complexas, facilitando a extensão (adicionar outras estratégias de ordenação) e seguindo o princípio de ````Open/Closed````.
  - Já em relação a recuperação dos produtos do nosso datasource, resolvi criar uma critéria que eu pudesse fazer uma pré-filtragem dos dados de produtos - atualmente essa lógica está em tempo de execução, porém, a ideia seria segregar essa responsabilidade para o database quando estivesse em ambiente produtivo - ou seja, com base nos parâmetros enviados na requisição, eu consigo filtrar somente os produtos que se encaixam efetivamente no filtro que o usuário está buscando.


### Estratégia técnica
- Em relação a stack, resolvi utilizar a que estou mais confortável ultimamente e que entendo que para este case, funcione bem. Kotlin está cada vez mais em enfâse no mercado atual, e juntamente com SpringBoot, um framework moderno e robusto, podemos criar soluções escaláveis e seguras visando o longo prazo.

