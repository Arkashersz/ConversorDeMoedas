# Conversor de Moedas

Um simples aplicativo de conversão de moedas com interface gráfica desenvolvido em Java usando JavaFX.

## Funcionalidades

- Conversão entre várias moedas baseadas nas taxas de câmbio atuais da API ExchangeRate-API.
- Interface de usuário intuitiva com seleção de moeda de base e moeda de destino, entrada de quantidade e exibição de resultados.

## Pré-requisitos

- JDK 11 ou superior instalado
- Conexão com a internet para acessar a API de taxas de câmbio

## Como Executar

1. Clone o repositório:

   ```bash
   git clone https://github.com/Arkashersz/ConversorDeMoedas.git

2. Navegue até o diretório do projeto:
    ```bash
    cd ConversorDeMoedas

3. Compile e execute o aplicativo usando o Maven:
    ```bash
    mvn clean compile javafx:run

4. Se você não tiver o Maven instalado localmente, pode usar o Maven Wrapper fornecido no projeto:
    ```bash
    ./mvnw clean compile javafx:run
    
Isso compilará o projeto e iniciará o aplicativo de conversão de moedas.

## Como Usar
1. Ao iniciar o aplicativo, você verá uma interface gráfica com opções para selecionar a moeda de base e a moeda de destino.

2. Insira a quantidade da moeda de base que você deseja converter no campo de entrada.

3. Clique no botão "Converter" para ver o resultado da conversão na área de texto abaixo.

## Recursos Utilizados

* **JavaFX:** Plataforma para construção de interfaces de usuário em Java.
* **Gson:** Biblioteca para desserialização de JSON em objetos Java.
* **HttpClient:** Para fazer requisições HTTP para a API de taxas de câmbio.
* **ExchangeRate-API:** API utilizada para obter as taxas de câmbio atualizadas.
  
## Estrutura do Projeto

- `src/`: Contém os arquivos fonte do projeto.
  - `main/`: Código-fonte principal.
    - `java/`: Arquivos Java.
    - `resources/`: Recursos como arquivos FXML, imagens, etc.
  - `test/`: Código de teste (se aplicável).
- `pom.xml`: Arquivo de configuração do Maven com dependências e plugins.

## Contribuição

Contribuições são bem-vindas! Se você quiser melhorar este projeto, siga estas etapas:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -am 'Adicionar Nova Feature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

## Licença

Este projeto está licenciado sob a [MIT License](https://opensource.org/licenses/MIT) - veja o arquivo [LICENSE](LICENSE) para mais detalhes.

