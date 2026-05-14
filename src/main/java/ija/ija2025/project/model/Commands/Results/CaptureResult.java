/**
 * Authors: Matej Čapek
 * Description: Result of the capture command.
 */

package ija.ija2025.project.model.Commands.Results;

public record CaptureResult(String previousOwner, int previousHP, boolean wasCaptured) {}