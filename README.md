#RRS#

stem, A and B, there is no connection bwteen the

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



