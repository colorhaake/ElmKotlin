# ElmKotlin
[Elm architecture](https://guide.elm-lang.org/) implementation in Kotlin.

Program flow:
1. AppModel -> local business logic -> LocalModel -> UI
2. UI -> local business logic -> LocalModel -> UI
3. UI -> local business logic -> global business logic -> AppModel

For example:
1. Initial counter value in AppModel -> send LocalMsg and update LocalModel -> show initial counter in UI
2. Click show example button -> send LocalMsg and launch example activity
3. Click add counter button -> send LocalMsg and send AppMsg to update AppModel

Check [sample app](https://github.com/colorhaake/ElmKotlin/tree/master/app/src/main/java/com/colorhaake/elmkotlin/sample) for more detail.
