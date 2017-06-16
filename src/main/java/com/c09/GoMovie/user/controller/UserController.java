package com.c09.GoMovie.user.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.c09.GoMovie.user.entities.User;
import com.c09.GoMovie.user.service.SessionService;
import com.c09.GoMovie.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@Api(value="用户模块", description="用户实体的CURD操作")
@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	SessionService sessionService;
	
	@ApiOperation(value="获取用户详细信息", notes="根据url的id来获取用户详细信息")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority({'admin', 'user'})")
    public User getUserById(@PathVariable("id") long id) {
    	return userService.getUserById(id);
    }
	
	@ApiOperation(value="获取用户列表", notes="")	
    @RequestMapping(value={"", "/"}, method=RequestMethod.GET)
    @PreAuthorize("hasAuthority('admin')")
    public Iterable<User> listUsers() {
    	return userService.listUsers();
    }
    
    // curl localhost:8080/api/users  -H "Content-Type: application/json" -d "{\"username\": \"test-user\", \"password\":\"test123\"}"
	@ApiOperation(value="创建用户", notes="根据User对象创建用户")
	@RequestMapping(value={"", "/"}, method=RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @PreAuthorize("permitAll()")
    public User createUser(@Valid @RequestBody User user) {
    	return userService.createUser(user);
    }
    

    @RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class UserServiceTest {
  
  @Autowired
  private UserService userService;
  
  @Autowired
  private UserRepository userRepository;
  
  @Rule
  public ExpectedException thrown = ExpectedException.none();
  
  @Before
  public void setUp() {
      userRepository.deleteAll();
  }
  
  @Test
  public void listUsers() {
      User user = new User();
      user.setUsername("test-list");
      user.setPassword("test-list");
      
      userRepository.save(user);

      assertTrue(((ArrayList<User>) userService.listUsers()).size() == 1);
  }
  
  
  @Test
  public void getUserById() {
      User user = new User();
      user.setUsername("test-get");
      user.setPassword("test-get");
      
      User expectedUser = userRepository.save(user);
      
      User actualUser = userService.getUserById(user.getId());
      
      assertEquals(expectedUser.getId(), actualUser.getId());
      assertEquals(expectedUser.getUsername(), actualUser.getUsername());
  }
  
  @Test
  public void createUser() {
      User user = new User();
      user.setUsername("test-create");
      user.setPassword("test-create");
      
      User actualUser = userService.createUser(user);
      
      User expectedUser = userRepository.findByUsername("test-create");
      
      assertEquals(expectedUser.getId(), actualUser.getId());
      assertEquals(expectedUser.getUsername(), actualUser.getUsername());
  }
  
  @Test
  public void updateUser() {
      User user = new User();
      user.setUsername("test-update");
      user.setPassword("test-update");
      user.setNickname("test-update");
      user = userRepository.save(user);

      assertEquals("test-update", user.getNickname());
      
      user.setNickname("test-update-again");
      
      User actualUser = userService.updateUser(user);
      
      User expectedUser = userRepository.findOne(user.getId());
      
      assertEquals(expectedUser.getNickname(), actualUser.getNickname());
  }
  
  @Test
  public void deleteUser() {
      User user = new User();
      user.setUsername("test-delete");
      user.setPassword("test-delete");
      user.setNickname("test-delete");
      user = userRepository.save(user);
      
      assertNotNull(userRepository.findByUsername("test-delete"));
      
      userService.deleteUserById(user.getId());
      
      assertNull(userRepository.findByUsername("test-delete"));
      
  }
  
  @Test
  public void loadUserByUsername() {
      User user = new User();
      user.setUsername("test-load");
      user.setPassword("test-load");
      userRepository.save(user);
      
      thrown.expect(UsernameNotFoundException.class);
      userService.loadUserByUsername("foobar");
      
      User actualUser2 = (User) userService.loadUserByUsername("test-load");
      
      assertNotNull(actualUser2);
  }
  
  @After
  public void tearDown() {
      userRepository.deleteAll();
  }
}
}
