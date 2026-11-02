export const palette = {
  light: {
    background: "#F7F8F5",
    surface: "#FFFFFF",
    surfaceElevated: "#FFFFFF",
    text: "#20251F",
    muted: "#737B70",
    border: "#E7EAE3",
    accent: "#587A50",
    accentSoft: "#EAF0E6",
    tab: "#FFFFFF"
  },
  dark: {
    background: "#111511",
    surface: "#1B211B",
    surfaceElevated: "#222A22",
    text: "#F0F3ED",
    muted: "#A2ADA0",
    border: "#303A30",
    accent: "#A7C79A",
    accentSoft: "#293729",
    tab: "#191F19"
  }
} as const;

export type ThemeMode = "light" | "dark";
export type ThemeColors = typeof palette.light;
export const spacing = { xs: 4, sm: 8, md: 16, lg: 24, xl: 32, xxl: 40 } as const;
export const radius = { sm: 10, md: 16, lg: 22, pill: 999 } as const;