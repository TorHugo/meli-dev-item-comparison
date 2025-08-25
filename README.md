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

### Documentation & strategic overview
#### Design da API
Para 