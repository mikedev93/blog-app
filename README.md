# Blog App

  

Blog App is a Native Android app developed using [Kotlin](https://kotlinlang.org/), an MVP architecture and Coroutines.

  

## Libraries

  

-  [Retrofit](http://square.github.io/retrofit/) : A type-safe REST client for Android which intelligently maps an API into a client interface using annotations.

-  [Room](https://developer.android.com/training/data-storage/room/) : Room provides an abstraction layer over SQLite to allow fluent database access while harnessing the full power of SQLite

-  [Glide](https://github.com/bumptech/glide) : Glide is a fast and efficient open source media management and image loading framework for Android that wraps media decoding, memory and disk caching, and resource pooling into a simple and easy to use interface.

  

## What it does

  

This App shows the latest news about **Watches**, using the [News API](https://newsapi.org/).

  

## Download and Install

Clone the proyect using:

  

```bash

git clone https://github.com/mikedev93/blog-app.git

```

Open proyect in Android Studio, and let all the file indexing and Gradle sync do the work.

Edit the ``API KEY`` atribute in ``APIConstants.kt`` with your own API Key, and just run the proyect!

  

## Some Highlights

*  **Endless Scroll**

When you reach the bottom of the list, more articles from the server are added.<br>
![https://media.giphy.com/media/GFwOKyrE1X5zEpXyCQ/giphy.gif](https://media.giphy.com/media/GFwOKyrE1X5zEpXyCQ/giphy.gif)

  

*  **Search Feature**
You can search through the remote and local articles.<br>
![Search Feature](https://media.giphy.com/media/njwaFk4tNu7EzytHXU/giphy.gif)

  

*  **Read Remote Articles**
Just click on any article, and it will take you to the source.<br>
![Read Remote Articles](https://media.giphy.com/media/UEb9q1CHwjVEqTzSHL/giphy.gif)

  

*  **Create Local Articles**
You can create a News Article that will get saved on a local SQLite Databse, all thanks to the [Room](https://developer.android.com/training/data-storage/room/) library.<br>
![Create Local Articles](https://media.giphy.com/media/H3Yx72ELreJUit6U7q/giphy.gif)

  

*  **Edit or delete your Local Articles**
Edit or delete your local news articles if you want.<br>
![Edit or delete your Local Articles](https://media.giphy.com/media/r8OX1e1d7b53LE68J4/giphy.gif)

  
  
  
  

## License

[MIT](https://choosealicense.com/licenses/mit/)
