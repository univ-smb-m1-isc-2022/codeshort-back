package com.example.codeshortbackend.repositories;

import com.example.codeshortbackend.models.Anecdote;
import com.example.codeshortbackend.models.AnecdoteReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnecdoteReportRepository extends JpaRepository<AnecdoteReport, Integer>  {
}
