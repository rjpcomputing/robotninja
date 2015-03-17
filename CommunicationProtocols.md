# Summary #

This document outlines the standards used for communication between the client, server, and robot.  These standards are to be used when implementing various aspects of the overall project.

# Purpose #

The purpose of these standards is to keep all aspects of the program on the same page.  This will allow us to keep communication between the different elements of the project solid.  We can then focus more on program logic instead of communication errors.  By following these communication protocols, each integration period should be made easier.

# Roles #

| **Client** | **Server** | **Robot** |
|:-----------|:-----------|:----------|
| Display video feed | Image processing | Receive commands |
| Display user interface | Receive commands from client | Execute commands |
| Send commands to server | Translate commands | Backtracking |
|  | Send translated commands to robot | Collision Detection |

# Protocols and Standards #

> ## Client-Server Communication ##
    * Client sends commands to server.
    * Client to server only commands are to be of length less than 10 characters.
> > An example of a client to server only command it the `score?` command. This command asks the server to give the current score. As you can see the command is only six characters long.
    * Client to designated to controlling the robot must strictly adhere to the following format:
> > `L[+|-]###R[+|-]###`


> Some examples of this regular expression in action are presented below. Remember, when you are looking at the robot from behind (sensor “eyes” pointing away from you), the left wheel is “L” and the right wheel is “R”. L+100 indicates rotating the left wheel forward at 100% speed. R-075 indicates rotating the right wheel backward at 75% speed. Here are some more examples:
| **Example** | **Description** |
|:------------|:----------------|
| `L+100R+100` | Full speed ahead |
| `L+075R+075` | Forward at 75% speed |
| `L-035R-035` | Backward at 35% speed |
| `L+100R+000` | Turn forward right at full speed (turning left tire forward makes the robot go forward to the right) |
| `L-100R-000` | Turn backward left at full speed |
| `L+000R+000` | Robot stops moving |

> As you can see, all commands must follow the 10 character length.
    * If client-to-server connectivity fails, the client will present an error message.
    * Server will acknowledge (ack) receipt of commands from client. A positive message response is `ACK`.
    * If the robot does not acknowledge (nack) or fails to respond to the server, an error is sent to the client. A negative message response is `NACK`.

> Additional commands (please note that all of these commands still follow the 10 character length):
> | 'x.........' | Disconnect |
|:-------------|:-----------|
> | 'o.........' | Open Claw |
> | 'c.........' | Close Claw |


> ## Server-Robot Communication ##
    * Server sends commands to robot after translating them into a format the robot can execute.
    * Robot will acknowledge (ack) receipt of commands from server. A positive message response is `true` (1). A negative message response is `false` (0).
    * If the robot is not available there will not be a response (ack/nack) from the robot.
      * This will trigger:
        * The server to retry the command again after 250ms.
        * The server will retry the command 5 times before sending a `nack` (`false`/0) to the client.