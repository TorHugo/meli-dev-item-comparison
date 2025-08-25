# Guia de Execução - Meli Item Comparison

Este documento fornece as instruções passo a passo para configurar e executar a aplicação `Meli - Item Comparison` em um ambiente de desenvolvimento local a partir do código-fonte fornecido.

### Pré-requisitos

Antes de começar, certifique-se de que você tem o seguinte software instalado em sua máquina:

-   **Java Development Kit (JDK)**: Versão 21 ou superior.

### 1. Configuração do Ambiente

Primeiro, descompacte o arquivo `.zip` do projeto em um diretório de sua preferência. Em seguida, abra um terminal (ou Prompt de Comando/PowerShell) e navegue até a pasta raiz do projeto que foi extraída.

**Exemplo:**

`````shell
# Navegue até o diretório onde você descompactou o projeto
cd path/to/dev-item-comparison
`````

### 2. Executando a Aplicação

O projeto utiliza o **Gradle Wrapper**, o que significa que você não precisa ter o Gradle instalado em sua máquina. O wrapper cuidará de baixar a versão correta e executar as tarefas necessárias.

Para compilar e iniciar a aplicação, utilize o script abaixo.

````shell
./gradlew bootRun
````


Aguarde o processo de build ser concluído. Ao final, você verá logs no terminal indicando que o servidor Spring Boot foi iniciado com sucesso, geralmente na porta `8080`.


### 3. Verificando se a Aplicação está no Ar

Com a aplicação em execução, você pode verificar seu funcionamento e testar os endpoints de duas maneiras:

#### A. Acessando a Documentação da API (Swagger UI)

A forma mais fácil de explorar e interagir com a API é através da interface do Swagger.

1.  Abra seu navegador de internet.
2.  Acesse a seguinte URL:
    **http://localhost:8080/api/swagger-ui/index.html#/**

Lá, você encontrará todos os endpoints documentados e prontos para serem testados diretamente pela interface.

#### B. Usando um Cliente HTTP (via Terminal com cURL)

Você também pode testar os endpoints diretamente pelo terminal. Abaixo estão alguns exemplos de comandos `cURL` baseados nos casos de uso principais.

**Exemplo 1: Buscar todos os produtos (paginado)**
````shell
curl -X GET "http://localhost:8080/api/v1/products/find-all?page=0&size=5"
````

**Exemplo 2: Buscar um produto específico por ID**
````shell
curl -X GET "http://localhost:8080/api/v1/products/1"
````

**Exemplo 3: Comparar produtos por preço (do mais barato para o mais caro)**
````shell
curl -X GET "http://localhost:8080/api/v1/products/comparison?category=notebooks" \
-H "type: PRICE" \
-H "higherIsBetter: false"
````

**Exemplo 4: Comparar produtos por especificação técnica (armazenamento, do maior para o menor)**
````shell
curl -X GET "http://localhost:8080/api/v1/products/comparison?category=notebooks" \
-H "type: SPECIFICATION" \
-H "specificationKey: armazenamento" \
-H "higherIsBetter: true"
````

