import { View, Text, StyleSheet, useColorScheme } from "react-native";
import { palette } from "../../src/design-system/tokens";
export default function InsightsScreen() {
 const mode=useColorScheme()==="dark"?"dark":"light"; const c=palette[mode];
 return <View style={[s.page,{backgroundColor:c.background}]}><Text style={[s.eyebrow,{color:c.muted}]}>REFLECT & LEARN</Text><Text style={[s.title,{color:c.text}]}>Insights</Text><View style={[s.card,{backgroundColor:c.surface,borderColor:c.border}]}><Text style={[s.cardTitle,{color:c.text}]}>Your consistency, over time</Text><Text style={{color:c.muted,lineHeight:22}}>As you record habits, LifeOS will surface patterns, trends and meaningful progress—not just streak counts.</Text></View></View>
}
const s=StyleSheet.create({page:{flex:1,padding:24,paddingTop:72,gap:12},eyebrow:{fontSize:10,letterSpacing:1.5,fontWeight:"700"},title:{fontSize:30,fontWeight:"700"},card:{padding:20,borderWidth:1,borderRadius:20,marginTop:12},cardTitle:{fontSize:17,fontWeight:"700",marginBottom:8}});