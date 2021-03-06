package com.camilo.treinos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.camilo.treinos.model.Material;
import com.camilo.treinos.repository.filter.MaterialFilter;
import com.camilo.treinos.service.CadastroMaterialService;

@Controller
@RequestMapping("/material")
public class MaterialController extends BaseController {

	private static final String CADASTRO_MATERIAL_VIEW = "cadastro-material";

	@Autowired
	CadastroMaterialService cadastroMaterialService;
	
	@RequestMapping("/novo")
	public ModelAndView novoMaterial() {
		
		ModelAndView mv = new ModelAndView(CADASTRO_MATERIAL_VIEW);
		mv.addObject("material", new Material());
		return mv;
	}
		
	@RequestMapping(method = RequestMethod.POST)
	public String salvarMaterial(@Validated Material material, Errors errors, RedirectAttributes attributes) {
		if (errors.hasErrors())
			return CADASTRO_MATERIAL_VIEW;
		
		try{
			cadastroMaterialService.salvar(material);
			attributes.addFlashAttribute("mensagem", "Material salvo com sucesso!");
			return "redirect:/material/" + material.getId();
		}
		catch (IllegalArgumentException e)
		{
			errors.rejectValue("dataAquisicao", null, e.getMessage());
			return CADASTRO_MATERIAL_VIEW;
		}		
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView pesquisarMaterial(@ModelAttribute("filtro") MaterialFilter filtro)
	{
		ModelAndView mv = new ModelAndView("pesquisa-material");
		mv.addObject("materiais", cadastroMaterialService.filtrar(filtro));
		return mv;
	}
	
	@RequestMapping("/{id}")
	public ModelAndView edicaoMaterial(@PathVariable("id") Material material)
	{
		ModelAndView mv = new ModelAndView(CADASTRO_MATERIAL_VIEW);
		mv.addObject(material);
		return mv;
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public String excluirMaterial(@PathVariable Long id, RedirectAttributes attributes)
	{
		cadastroMaterialService.excluir(id);
		attributes.addFlashAttribute("mensagem", "Título excluído com sucesso!");
		return "redirect:/material";
	}
}
