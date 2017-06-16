package com.c09.GoMovie.cinema.entities;


import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.c09.GoMovie.user.entities.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class CinemaComment {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	@NotNull
	@Min(value=0)
	@Max(value=10)
	@Column(nullable=false)
	private Integer score;

	@NotNull
	@Size(max=1024)
	@Column(nullable=false, length=1024)
	private String content;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;

	@ManyToOne(cascade = { CascadeType.MERGE,CascadeType.REFRESH }, optional = false)
    @JoinColumn(name="user_id")
	private User user;

	public CinemaComment() {
		setCreateTime(new Date(System.currentTimeMillis()));
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@ManyToOne(cascade = { CascadeType.MERGE,CascadeType.REFRESH }, optional = false)
    @JoinColumn(name="cinema_id")
	private Cinema cinema;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@JsonBackReference
	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	@RequestMapping(value={"/{id}"}, method=RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('admin')")
    public void updateMovie(@Valid @RequestBody Movie movie, @PathVariable("id") long id) {
    	movie.setId(id);
    	movieService.updateMovie(movie);
    }
    
    @RequestMapping(value={"/{id}"}, method=RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('admin')")
    public void deleteMovieById(@PathVariable("id") long id) {
    	movieService.deleteMovieById(id);
      }
      
    @RequestMapping(value="/{id}/comments", method = RequestMethod.GET)
    public List<MovieComment> getCommentsByMovieId(@PathVariable("id") long movieId) {
    	return movieService.findAllCommentsByMovieId(movieId);
    public List<MovieComment> listCommentsByMovieId(@PathVariable("id") long id) {
    	return movieService.listCommentsByMovieId(id);
    }
    
    @RequestMapping(value={"/{id}/comments"}, method=RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('admin', 'user')")
    public void createComment(@Valid @RequestBody MovieComment movieComment, @PathVariable("id") long id) {
    	User user = sessionService.getCurrentUser();
    	Movie movie = movieService.getMovieById(id);
    	movieService.createComment(movieComment, movie, user);
    }
    
    @RequestMapping(value={"/{movieId}/comments/{commentId}"}, method=RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('admin', 'user')")
    public void deleteCommentById(@PathVariable("commentId") long id) {
    	User user = sessionService.getCurrentUser();
    	movieService.deleteCommentById(user, id);
      }
  }

}
