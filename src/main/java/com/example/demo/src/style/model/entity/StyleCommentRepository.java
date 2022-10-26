package com.example.demo.src.style.model.entity;

import com.example.demo.config.Status;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StyleCommentRepository extends JpaRepository<StyleComment, Long> {
    Long countByStyleIdxAndStatus(Long idx, Status activated);

    List<StyleComment> findByStyleIdxAndStatus(Long parentIdx, Status status);

    List<StyleComment> findByStyleIdxAndParentIdxIsNotNullAndStatus(Long styleIdx,Status status, Sort sort);

    List<StyleComment> findByStyleIdxAndParentIdxIsNullAndStatus(Long styleIdx,Status status, Sort sort);

    @Query("SELECT sc.user.nickName FROM StyleComment sc where sc.idx = :parentIdx")
    String findNickNameByIdx(Long parentIdx);

    Optional<StyleComment> findByIdxAndStatus(Long commentIdx, Status status);
}
