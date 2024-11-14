# Orange-6
<h6>By Denis Krylov and Daan Schram</h6>
<br>
Welcome to the Orange-6 repository of the Pentago2D software product.
<br>
<br>
![img.png](https://github.com/denskrlv/AlphaPentaGo/blob/main/img.png?raw=true)

<br>

<h3>What is Pentago</h3>

Pentago is a two-player game where placing marbles and rotating sub boards may get a player to win.

<br>

<h3>What is this product</h3>
During the programming project at the University of Twente in January 2022, we created a software product for this game. 
This standalone software product contains of several elements: 

<ul>

<li>
Game itself (game logic) <br>
Two players are able to test and play this game logic in a class. Besides, there is a naive Ai and a smart Ai. These Ais can be used instead of a human. In this way, a human can play against the computer, or two computers can even compete against each other.
</li>

<br>

<li>
Client and a server. <br>
The server hosts the game and replies to specific comments from the client. The client gives the user information on what commands they can use, how to play a game, it passes commands to the server and it reads commands from the server on a separate thread. 
</li>

<br>

<li>
Unit and System Tests 
In the software product, there is Javadoc, comments and JML everywhere, meaning any future maintainer of the product is very well capable of understanding the product and improving it. All unit and system tests will be extra explained in the report
</li>
</ul>

<br>
<h3>How to use this product</h3>
This product has, as explained, several different elements. You can for instance use the product by running the Pentago.java file, which will give two people that are on the same machine the ability to play a game of Pentago. 

If you wish to play against other people, you can choose whether you want to host your own server or connect to an already existing server. If the last applies, then just run the Client.java and you will be able to connect and play. If you want your own server, run the ServerApplication.java file and the Client.java file. If you want the Ai that we created to play, then run the SmartComputerClient.java file.


