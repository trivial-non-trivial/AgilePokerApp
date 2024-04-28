
* execute
`
~/.local/share/coursier/bin/mill show __.sources
`

* result
```json
{
  "AgilePokerBackEndWSModule.sources": [
    "ref:v0:c984eca8:/__path-to-project__/AgilePokerApp/AgilePokerBackEndWSModule/src",
    "ref:v0:8573ac66:/__path-to-project__/AgilePokerApp/AgilePokerPublic",
    "ref:v0:b9eb7237:/__path-to-project__/AgilePokerApp/AgilePokerBackEndWS/App"
  ],
  "AgilePokerFrontEndModule.sources": [
    "ref:v0:c984eca8:/__path-to-project__/AgilePokerApp/AgilePokerFrontEndModule/src",
    "ref:v0:8573ac66:/__path-to-project__/AgilePokerApp/AgilePokerPublic",
    "ref:v0:f772d634:/__path-to-project__/AgilePokerApp/AgilePokerFrontEnd"
  ],
  "AgilePokerPublicModule.sources": [
    "ref:v0:c984eca8:/__path-to-project__/AgilePokerApp/AgilePokerPublicModule/src",
    "ref:v0:8573ac66:/__path-to-project__/AgilePokerApp/AgilePokerPublic"
  ]
}
```
