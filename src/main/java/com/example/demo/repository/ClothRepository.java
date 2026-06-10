package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Cloth;

@Repository
public interface ClothRepository extends JpaRepository<Cloth, Long> {
	// タイトルでの部分一致検索メソッドを自動生成
	List<Cloth> findByNameContaining(String keyword);
}
