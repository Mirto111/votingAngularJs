package myProject.voting.model;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Secret_Hero on 24.10.2017.
 */
@Entity
@Table(name = "vote")
public class Vote extends BaseEntity {

    private static ConcurrentHashMap<Integer, String> voteCount = new ConcurrentHashMap<>();

    @Column(name = "vote_date", nullable = false)
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate votingDate;

    @Column(name = "rest_name", nullable = false)
    @NotBlank
    private String restaurantName;

    @Column(name = "count", nullable = false)
    private int count;

    public Vote() {
    }

    public Vote( String restaurantName, int count) {
       this(null,LocalDate.now() ,restaurantName,count);
    }

    public Vote(Integer id, LocalDate votingDate, String restaurantName, int count) {
        super(id);
        this.votingDate = votingDate;
        this.restaurantName = restaurantName;
        this.count = count;
    }


    public LocalDate getVotingDate() {
        return votingDate;
    }

    public void setVotingDate(LocalDate votingDate) {
        this.votingDate = votingDate;
    }

    public static ConcurrentHashMap<Integer, String> getVoteCount() {
        return voteCount;
    }

    public static void setVoteCount(ConcurrentHashMap<Integer, String> voteCount) {
        Vote.voteCount = voteCount;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    @Override
    public String toString() {
        return "Vote{" +
                "votingDate=" + votingDate +
                ", restaurantName='" + restaurantName + '\'' +
                ", count=" + count +
                '}';
    }
}
