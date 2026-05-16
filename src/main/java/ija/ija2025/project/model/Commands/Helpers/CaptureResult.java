/**
 * Authors: Matej Čapek
 * Description: Result of the capture command.
 */

package ija.ija2025.project.model.Commands.Helpers;

public record CaptureResult(String previousOwner, int previousHP, boolean wasCaptured) {}