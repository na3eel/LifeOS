export type LifeArea = {
  id: string;
  name: string;
  subtitle: string;
  symbol: string;
  habitCount: number;
  completedCount: number;
  color: string;
};

export const lifeAreas: LifeArea[] = [
  { id: "body", name: "Body", subtitle: "Health, movement & energy", symbol: "◒", habitCount: 3, completedCount: 0, color: "#8DAA82" },
  { id: "mind", name: "Mind", subtitle: "Learning, focus & creativity", symbol: "✳", habitCount: 2, completedCount: 0, color: "#B49BCB" },
  { id: "soul", name: "Soul", subtitle: "Reflection & inner balance", symbol: "☼", habitCount: 2, completedCount: 0, color: "#D1A66D" },
  { id: "social", name: "Social", subtitle: "Family, friends & connection", symbol: "⌁", habitCount: 1, completedCount: 0, color: "#83A9B8" },
  { id: "career", name: "Career", subtitle: "Meaningful work & growth", symbol: "↗", habitCount: 2, completedCount: 0, color: "#C58E8A" }
];