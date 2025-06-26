package net.hovering.Hovering_Web.join.repository;

import net.hovering.Hovering_Web.join.model.Join;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JoinRepository extends JpaRepository<Join, Long> {
}