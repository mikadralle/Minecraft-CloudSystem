package eu.unyfy.cloudsystem.util;

public final class StringUtil {

  public static boolean isMemberOf(final String original, final char... chars) {

    boolean state = true;

    for (final char c : original.toCharArray()) {

      boolean otherState = false;

      for (final char aChar : chars) {
        if (c == aChar) {
          otherState = true;
          break;
        }
      }

      if (!otherState) {
        state = false;
        break;
      }
    }

    return state;
  }

  public static boolean isOnlyMemberOf(final String original, final char character) {

    for (final char c : original.toCharArray()) {
      if (c != character) {
        return false;
      }
    }

    return true;
  }
}
