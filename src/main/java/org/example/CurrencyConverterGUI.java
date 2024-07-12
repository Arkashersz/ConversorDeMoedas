import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CurrencyConverterGUI extends Application {

    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";

    private ComboBox<CurrencyOption> baseCurrencyComboBox;
    private ComboBox<CurrencyOption> targetCurrencyComboBox;
    private TextField amountTextField;
    private TextArea conversionsTextArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Conversor de Moedas");

        // Layout principal usando GridPane
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Labels e controles
        Label baseCurrencyLabel = new Label("Moeda de Base:");
        GridPane.setConstraints(baseCurrencyLabel, 0, 0);

        baseCurrencyComboBox = new ComboBox<>();
        baseCurrencyComboBox.setPromptText("Selecione");
        GridPane.setConstraints(baseCurrencyComboBox, 1, 0);

        Label targetCurrencyLabel = new Label("Moeda de Destino:");
        GridPane.setConstraints(targetCurrencyLabel, 0, 1);

        targetCurrencyComboBox = new ComboBox<>();
        targetCurrencyComboBox.setPromptText("Selecione");
        GridPane.setConstraints(targetCurrencyComboBox, 1, 1);

        Label amountLabel = new Label("Valor:");
        GridPane.setConstraints(amountLabel, 0, 2);

        amountTextField = new TextField();
        GridPane.setConstraints(amountTextField, 1, 2);

        Button convertButton = new Button("Converter");
        GridPane.setConstraints(convertButton, 0, 3);

        conversionsTextArea = new TextArea();
        conversionsTextArea.setEditable(false);
        GridPane.setConstraints(conversionsTextArea, 0, 4, 2, 1);

        // Carregar opções de moedas
        loadCurrencyOptions();

        // Ação do botão de conversão
        convertButton.setOnAction(e -> {
            try {
                CurrencyOption baseCurrencyOption = baseCurrencyComboBox.getValue();
                CurrencyOption targetCurrencyOption = targetCurrencyComboBox.getValue();
                double amount = Double.parseDouble(amountTextField.getText());

                // Obter as taxas de câmbio da API
                String jsonResponse = getApiResponse(API_URL + baseCurrencyOption.getCode());

                // Desserializar a resposta JSON
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

                // Obter a taxa de câmbio da moeda de destino
                if (jsonObject.getAsJsonObject("rates").has(targetCurrencyOption.getCode())) {
                    double rate = jsonObject.getAsJsonObject("rates").get(targetCurrencyOption.getCode()).getAsDouble();
                    double convertedAmount = amount * rate;

                    String baseCurrencyDescription = getCurrencyDescription(baseCurrencyOption.getCode(), amount);
                    String targetCurrencyDescription = getCurrencyDescription(targetCurrencyOption.getCode(), convertedAmount);

                    String conversionResult = String.format("%.2f %s é igual a %.2f %s%n",
                            amount, baseCurrencyDescription, convertedAmount, targetCurrencyDescription);
                    conversionsTextArea.appendText(conversionResult);
                } else {
                    conversionsTextArea.appendText("Moeda de destino inválida ou não suportada.\n");
                }
            } catch (Exception ex) {
                conversionsTextArea.appendText("Erro durante a conversão.\n");
                ex.printStackTrace();
            }
        });

        // Adicionar todos os controles ao layout
        grid.getChildren().addAll(baseCurrencyLabel, baseCurrencyComboBox,
                targetCurrencyLabel, targetCurrencyComboBox,
                amountLabel, amountTextField,
                convertButton, conversionsTextArea);

        // Exibir a cena
        Scene scene = new Scene(grid, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadCurrencyOptions() {
        try {
            // Moedas desejadas
            List<String> desiredCurrencies = Arrays.asList("USD", "AUD", "CAD", "JPY", "EUR", "GBP", "MXN", "CNH", "BRL");

            // Obter as taxas de câmbio da API para carregar as moedas disponíveis
            String jsonResponse = getApiResponse(API_URL + "USD");

            // Desserializar a resposta JSON
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

            // Obter todas as moedas disponíveis e suas descrições
            List<CurrencyOption> currencyOptions = new ArrayList<>();
            jsonObject.getAsJsonObject("rates").entrySet().forEach(entry -> {
                String currencyCode = entry.getKey();
                if (desiredCurrencies.contains(currencyCode)) {
                    String currencyDescription = getCurrencyDescription(currencyCode, 1); // Singular por padrão
                    currencyOptions.add(new CurrencyOption(currencyCode, currencyDescription));
                }
            });

            // Carregar opções nos ComboBoxes
            baseCurrencyComboBox.getItems().addAll(currencyOptions);
            targetCurrencyComboBox.getItems().addAll(currencyOptions);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getApiResponse(String apiUrl) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // Método para obter a descrição da moeda com concordância
    private String getCurrencyDescription(String currencyCode, double amount) {
        switch (currencyCode) {
            case "USD":
                return amount == 1 ? "Dólar Americano" : "Dólares Americanos";
            case "EUR":
                return amount == 1 ? "Euro" : "Euros";
            case "GBP":
                return amount == 1 ? "Libra Esterlina" : "Libras Esterlinas";
            case "AUD":
                return amount == 1 ? "Dólar Australiano" : "Dólares Australianos";
            case "CAD":
                return amount == 1 ? "Dólar Canadense" : "Dólares Canadenses";
            case "JPY":
                return amount == 1 ? "Iene Japonês" : "Ienes Japoneses";
            case "MXN":
                return amount == 1 ? "Peso Mexicano" : "Pesos Mexicanos";
            case "CNH":
                return amount == 1 ? "Yuan Chinês" : "Yuanes Chineses";
            case "BRL":
                return amount == 1 ? "Real Brasileiro" : "Reais Brasileiros";
            default:
                return "Descrição não disponível";
        }
    }

    // Classe para representar uma opção de moeda com código e descrição
    private static class CurrencyOption {
        private String code;
        private String description;

        public CurrencyOption(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return code + " (" + description + ")";
        }
    }
}
