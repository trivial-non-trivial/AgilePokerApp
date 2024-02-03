**Back end**
- mill
- cask
- upickle

**Front end**
- mill
- scalajs
- laminar
- laminext
- upickle

**Public**
- upickle

**build & launch**
- open a terminal for the back
  - install mill (via coursier)
  - move in directory AgilePokerApp
  - execute the command "~/.local/share/coursier/bin/mill AgilePokerBackEndWSModule"
- open a terminal for the front
  - move in directory AgilePokerApp
  - execute the command "~/.local/share/coursier/bin/mill AgilePokerFrontEndModule.fastOpt"
  - open "AgilePokerFrontEnd/src/main/resources/index.html" in a browser 
