package myProject.voting.repository.datajpa;


import myProject.voting.model.VotingResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CrudVoteRepository extends JpaRepository<VotingResult,Integer> {

    @Override
    <S extends VotingResult> List<S> saveAll(Iterable<S> entities);

    List<VotingResult> findAllByVotingDate(LocalDate localDate);
}
