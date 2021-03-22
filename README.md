# Currency Converter
Android app demonstrating https://currencylayer.com/ API
- Kotlin + DataBinding + ViewModel + Repository Later (Room + Retrofit)

### Installation
- Pull the project and add CurrencyLayer API key into $HOME/.gradle/gradle.properties file:
```
CURRENCY_LAYER_API_KEY=xxxxxxxxxxxxxxxxxxxxxxxxx
```

### Features
 - Gets updated currency list, cached for 24 hours
 - Calculate exchange amounts for any entered value
 - Calculate exchange amounts when selected a new currency
 - Cache exchange rates every 30 minutes

### Limitations
- When using a standard account, exchange rate is limited to USD only.

### TODOs
- Improve API error catching and messages displayed to user.
- Improve UI
- Add unit testing
