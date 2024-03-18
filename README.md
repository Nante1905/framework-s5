-format mba atao milailay amlay Controller
-ilay App tsy atao test tsony

- génération front:
  \*génération import: (anaty Test.java misy exemple)
  \_ ohatra hoe ampiana <import> eo amlay template pour indiquer hoe mila manao génération import XD
  \_mampiasa FrontGeneration.generateImport
  \_ilay import type mila atao manuel:

  - ampiana an'ilay type anaty PageImport[] zay vao antsoina ilay generateImport

- gestion import amlay form satria dynamique
  -mnu koa mila ovaina ilay nom app

  - mila ovaina jackson json lay gson (anaty toJson handyman) fa tsy haiko mampiditra jar an'ilay jackson anaty jar an'ilay handyman ref buildena lay izy

  generate form:

- String generateForm(Entity e)
- alaina ilay template
- alaina template input, inputNumber, select, date
- alaina template foreignKeyGetter
- manao var String inputs, foreignKey, pageImport
- tetezina ny e.getFields() d ampiana ny var inputs
  - si isForeignKey false
    +si type = date => alaina template date, ovaina ny variable ao (inputLabel) d manampy pageImport
    +si Integer ou int => alaina template inputNumber, ovaina ny variable ao (inputLabel) d manampy pageImport  
     +sinon => alaina template input, ovaina ny variable ao (inputLabel) d manampy pageImport
    \*sinon :
    - alaina template select, ovaina variable (inputLabel, entityFkField, entityFkPk)
    - alaina template foreignKeyGetter, ovaina ny variable ao (entityMin) d ampiana ny var foreignKey
- ny entityMaj ovaina entityMaj
- FrontPage.addImport(pageImport)
- ny <import>, <foreignKey>, <input> anaty template ovaina
- generer fichier

- ilay status amin'ilay exception
- génération code asiana meethod ny URLMapping

front: json tsotra no alefa amlay post sy put

- bug import
- génération interface
- lay landing page sy fanovana mbola tsy tafiditra

RESTE A FAIRE

- PB FK: ilay PageInfo
- pico cli
- test general
