
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



