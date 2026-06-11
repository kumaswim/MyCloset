package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Category;
import com.example.demo.model.Cloth;
import com.example.demo.repository.ClothRepository;

@Controller
@RequestMapping("/clothes")
public class ClothController {

	@Autowired
	private ClothRepository clothRepository;
	
	// 一覧表示 & カテゴリ絞り込み
	@GetMapping
	public String listClothes(@RequestParam(value = "category", required = false) String category, Model model) {
		List<Cloth> clothes = (category != null && !category.isEmpty()) ? 
				clothRepository.findByCategory(category) : clothRepository.findAll();
				
		model.addAttribute("clothes", clothes);
		model.addAttribute("newCloth", new Cloth());
		model.addAttribute("categories", Category.values()); 
		return "clothes";
	}
		
	// 新規登録 & 更新
	@PostMapping("/save")
	public String saveCloth(@ModelAttribute("newCloth") Cloth cloth,
	                        @RequestParam(value = "selectedAttributes", required = false) List<String> attrs) {
		
		// チェックボックスが選ばれていたら、カンマ区切りの文字列にしてセットするお！
		if (attrs != null && !attrs.isEmpty()) {
			cloth.setAttribute(String.join(",", attrs));
		} else {
			cloth.setAttribute(""); // 1つも選ばれてないときは空文字
		}
		
		clothRepository.save(cloth);
		return "redirect:/clothes";
	}

	// 編集画面用データの取得
	@GetMapping("/edit/{id}")
	public String editClothForm(@PathVariable("id") Long id, Model model) {
		Cloth cloth = clothRepository.findById(id).orElseThrow();
		model.addAttribute("categories", Category.values());
		model.addAttribute("clothes", clothRepository.findAll());
		model.addAttribute("newCloth", cloth); 
		return "clothes";
	}

	// 削除
	@GetMapping("/delete/{id}")
	public String deleteCloth(@PathVariable("id") Long id) {
		clothRepository.deleteById(id);
		return "redirect:/clothes";
	}

	// コーディネート自動生成
	@GetMapping("/coordinate")
	public String showCoordinateResult(@RequestParam(name = "attribute", required = false) String attribute, Model model) {
		List<Cloth> allClothes = clothRepository.findAll();

		List<String> attributes = allClothes.stream()
				.map(Cloth::getAttribute)
				.filter(attr -> attr != null && !attr.isEmpty())
				.flatMap(attr -> java.util.Arrays.stream(attr.split(","))) 
				.distinct()
				.collect(Collectors.toList());
		model.addAttribute("attributes", attributes);
		model.addAttribute("selectedAttr", attribute);

		if (attribute != null && !attribute.isEmpty()) {
			List<Cloth> filteredClothes = allClothes.stream()
					.filter(c -> c.getAttribute() != null && c.getAttribute().contains(attribute))
					.collect(Collectors.toList());

			List<Cloth> topsList = filteredClothes.stream()
					.filter(c -> "TOPS".equals(c.getCategory()))
					.collect(Collectors.toList());
			java.util.Collections.shuffle(topsList);

			List<Cloth> bottomsList = filteredClothes.stream()
					.filter(c -> "BOTTOMS".equals(c.getCategory()))
					.collect(Collectors.toList());
			java.util.Collections.shuffle(bottomsList);

			java.util.Optional<Cloth> selectedTop = topsList.stream().findFirst();
			java.util.Optional<Cloth> selectedBottom = bottomsList.stream().findFirst();

			// 🚨 緊急フォールバック
			Cloth finalTop = selectedTop.orElseGet(() -> {
				Cloth emergencyTop = new Cloth();
				emergencyTop.setId(0L);
				emergencyTop.setName("絆創膏（バツ印で2枚）");
				emergencyTop.setCategory("TOPS");
				emergencyTop.setAttribute("緊急避難");
				return emergencyTop;
			});

			Cloth finalBottom = selectedBottom.orElseGet(() -> {
				Cloth emergencyBottom = new Cloth();
				emergencyBottom.setId(0L);
				emergencyBottom.setCategory("BOTTOMS");
				emergencyBottom.setAttribute("野生");

				String[] emergencyItems = {"大きな葉っぱ一枚", "ピンクのリボン（紐縛り）", "白いガムテープ"};
				int randomIndex = new java.util.Random().nextInt(emergencyItems.length);
				emergencyBottom.setName(emergencyItems[randomIndex]);
				return emergencyBottom;
			});

			model.addAttribute("selectedTop", finalTop);
			model.addAttribute("selectedBottom", finalBottom);
			model.addAttribute("clothes", filteredClothes);
		} else {
			model.addAttribute("clothes", allClothes);
		}

		return "coordinate-result"; // 💡 ここだけお兄ちゃんの指定に合わせたよ！
	}
}