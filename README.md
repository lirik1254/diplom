# Серверная часть проекта в рамках преддипломной практики

* Для запуска приложения в корне проекта введите в командной строке
```
docker-compose up --build -d
```
* !! Для запуска проекта необходимо определить переменные среды окружения:
  - SPRING_SECURITY_PASSWORD - пароль для user в spring security
  - SPRING_MAIL_USERNAME - логин от smtp.yandex.ru сервера
  - SPRING_MAIL_PASSWORD - пароль от smtp.yandex.ru сервера
  - JWT_SECRET - секретный ключ JWT
  - YANDEX_CLOUD_KEY_ID - идентификатор ключа в yandex cloud storage
  - YANDEX_CLOUD_SECRET_KEY - секретный ключ в yandex cloud storage
  - YANDEX_CLOUD_BUCKET - бакет для записи данных
* После необходимо запустить мобильное приложение -> https://github.com/katenadolgih/ProTim/tree/final-version
