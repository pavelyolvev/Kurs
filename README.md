# Kurs
Установка и запуск:
  1) Скомпилировать и собрать в jar архив каждый из проектов в intellij idea с помощью maven
![image](https://github.com/pavelyolvev/Kurs/assets/31282106/4ab9331a-ad62-470a-8c0d-af1673585da8)

  2) В папку System положить скомпилированные и собранные в пакет файлы.

  3) В папку lib положить javaFX
  
    https://download2.gluonhq.com/openjfx/20.0.1/openjfx-20.0.1_linux-x64_bin-sdk.zip

  4) Запустить приложение с помощью команды java --module-path ./lib --add-modules javafx.controls,javafx.fxml -jar "./kSuperApp.jar"
  
  (Если требуется использовать съемный носитель в приложении, то необходимо выполнять команду с правами администратора)
