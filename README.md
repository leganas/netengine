# netengine


Сетевой движок на основе kryo.net иннкапсулирующий в себе набор решений:

- Описывает абстрактные классы и интерфейсы, обобщающие структуру передаваемых данных по сети
- Реализует контроллеры Server, Client (Которые берут на себя организацию 
  всей инфраструктуры общения Client-Server с очередью событий, и обратными вызовами для UI)
  
  ## Maven Build
  
```xml
  <dependency>
  <groupId>by.legan.library</groupId>
  <artifactId>NetEngine</artifactId>
  <version>1.8</version>
  </dependency>  
```

PS. Движок на этапе разработки и будет расширять свой функционал по мере необходимости (при реализации моих проектов)
