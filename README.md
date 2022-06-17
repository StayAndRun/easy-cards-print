# easy-cards-print
Packs separate images to multi-page PDF file for print

### Card catalog structure:

    .
    ├── <project-name>          # Your game project name
    │   ├── decks               # Reserved directory name for card decks
    │   │   ├── <deck-name-1>
    │   │   │   ├── <card-front-image-1>
    │   │   │   ├── <card-front-image-2>
    │   │   │   ├── <card-front-image-3>
    │   │   │   ├── ...
    │   │   │   └── backs
    │   │   │       ├── <deck-back-image-1>
    │   │   │       ├── <deck-back-image-2>
    │   │   │       └── ...
    │   │   ├── <deck-name-2>
    │   │   │   ├── ...
    │   │   │   └── ...
    │   │   └── ...
    │   └──


#### For example:
        .
    ├── trading-card-game         
    │   ├── decks               
    │   │   ├── black-deck
    │   │   │   ├── skeleton.svg
    │   │   │   ├── zombie.svg
    │   │   │   └── backs
    │   │   │       └── black back.svg
    │   │   ├── green-deck
    │   │   │   ├── spider.svg
    │   │   │   └── wolf.svg
    │   │   │   └── backs
    │   │   │       └── green back.svg
    │   │   └── ... 
    │   └──