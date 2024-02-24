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
    - start serving at <http://localhost:8080/>
- open a terminal for the front
  - move in directory AgilePokerApp
  - execute the command
    - "~/.local/share/coursier/bin/mill clean AgilePokerFrontEndModule &&  ~/.local/share/coursier/bin/mill AgilePokerFrontEndModule.buildAndCopy"
  - open <http://localhost:8080/agilePoker/Room-001/index.html> in a browser 
