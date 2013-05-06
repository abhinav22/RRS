#RRS#

##Feature List

1. Register
  * register by input email, password. After registered, a email verification will be sent by email, user have to activate the account by links provided in the email.
  * register form an invitation, there is a little difference. From an invitation, the registration can be considered as a human operation, and  the email verification will be ignored. And the connection will be created between the two users automatically.
2. Login
  * Login by registered email and password.
  * If it is the first time to log in this application, user is asked for filling in the basic user info.
3. Profile
  * After user logged in at the first time, and user filled the basic info, a basic profile is created.
  * User can edit the profile info after logged in.
  * User can change password after logged in.
  * User can set an avatar image after logged in.
  * A public user profile is also provided for all users.
4. Resource  
  * User can share resource, also provide edit and delete resource.
  * A view detailed resource info page is provided, all comments for this resource is listed in the page, and user can "Like" or comment on resource in this page. 
5. Rate
  * A "Like" feature is implemented for resource rating.
  * "Like" is a also a bookmark, User also can view the resource he had "liked".
  * User can "Like" and comment the resource recommendation after he filled the basic info when logged in the system at the first time.
  * User can simply "Like" or comment on resource in recommendation list.
  * A link is provided, user can view the detailed resource info 
  * Comment to resource is provided in several entries, in the recommendation list, and the resource detail page.
6. Search user and resource 
  * User can search user.
  * User can view the profile of the user search result.
  * User view log is recorded, and a basic statistics of "viewed by others" is provided. 
  * User can search resource.
8. Invite 
  * User can invite users to join the app by email.
  * Multi email addresses can be added at the same time.
  * Existed email will be checked and should be ignored.
  * User can preview the invitation email content, and add extra info for the invitation.
  * The invitation email content will provide link to guide user to register.
9. Connect
  * User can send a connection request to others in the app.
  * A user can accept or ignore the connection request from others.
  * In the user public profile page, the connection status info will be displayed.
  * In user home page, there is a page to display the connections of user.
  * There is a page to display the pending connection sent by others in the application. 


##Features

###Create a Connection between A and B

Assume there are two users in the system, A and B, there is no connection bwteen them.

A search users in the "People" tab, and get the B in the list.

Click view profile to the profile details of B.

If there is no connection between them, on the top of the B's profile, there should display a "Send Connection" button for A.

A can send connection request by click the "Send" button. A connection request will be sent soon. And at the same time, The "Send" button is disappeared,  A should see the connection status in B's profile is changed to "waiting for B's response". 

A is waiting for the response of B now.

When B logged in the system, he will find there is a pending connection from A in home page. In the user home page, there is a basic statistics for the pending connections.

Click it to display all pending connection from others.

B can select "Accept" or "Ignore" all the pending connection request one by one.

If B "accept" the connection from A, there is connection created between A and B.

In the user home of A and B, there is another statistics of "Connection" they have created, click it and it will display all connections of the logged user, they both can see each other in the "Connection" page.

If B "ignore" the connection, the connection is ignored at all. There is no any connection info between A and B, they can create new connection if they like.


##Build Project     

Clone the codes from github, [https://github.com/abhinav22/RRS.git](https://github.com/abhinav22/RRS.git).

<pre>
git clone https://github.com/abhinav22/RRS.git
</pre>

More detailed setup steps, please refer to the wiki page [Developer Guide](https://github.com/abhinav22/RRS/wiki/Development-Setup-Guide).

##Overview of Source Code

All of the codes follows the multi layered architecture.

MongoDb is selected as datastore for this project.

Spring Data Mongo is used for MongoDB operations.

Spring MVC is used as MVC dispatcher.

In the presentation layer, JSP and Tiles are used for rendering the page.

We also adopt the AngularJS and REST API(also produced by Spring MVC) to improve the user experience.

The classes for different purpose are categorized in different package.

`com.example.rrs.model` includes all models in this project.


<pre>
@Docuemnt
public class User{

	@Id
	private String id;
}
</pre>

`com.example.rrs.repository` includes repositories for the models.


<pre>
 public interface UserRepository extends MongoRepository&lt;User, String>{
 }
</pre>

We also use [QueryDSL](http://www.querydsl.org) which provides fluent and type safe API for entity query. The querydsl maven APT plugin will generate the typesafe metamodel for all models. 

`com.example.rrs.service` includes the service class.

`com.example.rrs.web` includes all Spring MVC controllers and command/model object for the controllers.

The Controllers are annotated with `@Controller`, `@RequestMapping` specify the mapping url and others http related configuration.

<pre>
@Controller
@RequestMapping("/user")
public class UserController{
}
</pre>

`com.example.rrs.api` includes the REST API which are used with AngularJS.

In the REST API producing, we also use Spring MVC, the controllers are very similar wtih the ones above.

The main difference in REST interface, we always consume and produce JSON or XML instead of HTML.

<pre>
@Controller
@RequestMapping("/api/user")
public class UserResource{

	@RequestMapping(value = "/{id}", produces="application/json")
	public String read(@PathVarible("id") String Id){
	}
}
</pre>



