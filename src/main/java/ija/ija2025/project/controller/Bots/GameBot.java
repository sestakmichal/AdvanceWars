/**
 * Authors: Michal Šesták
 * Description: Interface for the game bot.
 */

package ija.ija2025.project.controller.Bots;

import ija.ija2025.project.model.Game.Game;

public interface GameBot {
    void executeTurn(Game game);
}