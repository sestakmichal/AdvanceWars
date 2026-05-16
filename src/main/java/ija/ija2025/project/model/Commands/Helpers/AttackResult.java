/**
 * Authors: Matej Čapek
 * Description: Result of the attack command.
 */

package ija.ija2025.project.model.Commands.Helpers;

public record AttackResult(int hpBefore, int attackerHpBefore, boolean destroyed, boolean attackerDestroyed) {}