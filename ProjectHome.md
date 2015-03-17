This is a software system that provides a user Internet access to an autonomous soft realtime robot in a remote location.

The entire project has three main parts:
  * Client - The web-based frontend that allows the user to control the robot.
  * Server/application - Mediator between the client and the robot.
  * Robot - A Lego MindStorms NXT based robot. It should handle events like collision with objects in its environment and loss of communication contact with the server without user interaction.