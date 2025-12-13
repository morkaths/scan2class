package com.morkath.scan2class.core;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.morkath.scan2class.dto.AssetDto;

public abstract class BaseController {
	
	@ModelAttribute("assets")
    public AssetDto defaultAssets() {
        return new AssetDto("Default Title");
    }
	
	/**
	 * Prepare the page with content and title
	 * @param model - The model to add attributes to
	 * @param page - The content page to be displayed
	 * @param title - The title of the page
	 */
	protected void preparePage(Model model, String page, String title) {
        model.addAttribute("content", page);
        model.addAttribute("assets", new AssetDto(title));
    }
	
	/**
	 * Prepare the page with content and assets
	 * @param model - The model to add attributes to
	 * @param page - The content page to be displayed
	 * @param assets - The AssetDto containing page assets
	 */
	protected void preparePage(Model model, String page, AssetDto assets) {
		model.addAttribute("content", page);
		model.addAttribute("assets", assets);
	}
	
}
