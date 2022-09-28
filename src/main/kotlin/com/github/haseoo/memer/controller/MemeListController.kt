package com.github.haseoo.memer.controller

import com.github.haseoo.memer.controller.view.MemeView
import com.github.haseoo.memer.repository.MemeRepository
import net.dv8tion.jda.api.JDA
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
class MemeListController(
    @org.springframework.context.annotation.Lazy private val jda: JDA?,
    private val memeRepository: MemeRepository) {
    @GetMapping(path = ["/memes/{serverId}"])
    fun getMemes(@PathVariable serverId: Long, @RequestParam sortByCount: Boolean?): ModelAndView {
        val memeViews = memeRepository
            .getMemes(serverId)
            .map { MemeView(it) }
        val modelAndView = ModelAndView("fragments/memeList.html")
        modelAndView.addObject("serverName", jda
            ?.getGuildById(serverId)
            ?.name
            ?: "the server")
        modelAndView.addObject("memes",
            if (sortByCount == true) memeViews.sortedByDescending { it.count } else memeViews.sortedBy { it.name })
        modelAndView.addObject("sortBy", if(sortByCount == true) "by popularity" else "by name")
        return modelAndView
    }
}