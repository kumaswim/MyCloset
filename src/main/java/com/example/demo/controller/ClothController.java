package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Cloth;
import com.example.demo.repository.ClothRepository;

@Controller
@RequestMapping("/clothes")

public class ClothController {
	@Autowired
	private ClothRepository clothRepository;

	// 一覧表示 & 検索
	@GetMapping
	public String listClothes(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
		List<Cloth> clothes = (keyword != null && !keyword.isEmpty()) ? 
		clothRepository.findByNameContaining(keyword) : clothRepository.findAll();
		model.addAttribute("clothes", clothes);
		model.addAttribute("newCloth", new Cloth()); // 登録用空オブジェクト
		model.addAttribute("keyword", keyword);
		return "clothes";
	}

	// 新規登録 & 更新（IDがあれば自動で更新になる）
	@PostMapping("/save")
	public String saveCloth(@ModelAttribute("newCloth") Cloth cloth) {
		clothRepository.save(cloth);
		return "redirect:/clothes";
	}

	// 編集画面用データの取得（今回は簡易的に同じ画面にデータを戻す）
	@GetMapping("/edit/{id}")
	public String editClothForm(@PathVariable("id") Long id, Model model) {
		Cloth cloth = clothRepository.findById(id).orElseThrow();
		model.addAttribute("clothes", clothRepository.findAll());
		model.addAttribute("newCloth", cloth); // 編集対象のデータをフォームに渡す
		return "clothes";
	}

	// 削除
	@GetMapping("/delete/{id}")
	public String deleteCloth(@PathVariable("id") Long id) {
		clothRepository.deleteById(id);
		return "redirect:/clothes";
	}
	
	@GetMapping("/coordinate")
	public String showCoordinatePage(Model model) { // 💡 引数に (Model model) を追加！
	    return "coordinate-result"; 
	}
}