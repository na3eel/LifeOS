import { View, Text, StyleSheet, useColorScheme } from "react-native";
import { palette } from "../../src/design-system/tokens";
export default function CoachScreen() {
 const mode=useColorScheme()==="dark"?"dark":"light"; const c=palette[mode];
 return <View style={[s.page,{backgroundColor:c.background}]}><Text style={[s.eyebrow,{color:c.muted}]}>A THOUGHTFUL COMPANION</Text><Text style={[s.title,{color:c.text}]}>Your coach</Text><View style={[s.card,{backgroundColor:c.surface,borderColor:c.border}]}><Text style={[s.symbol,{color:c.accent}]}>✳</Text><Text style={[s.cardTitle,{color:c.text}]}>Make space for reflection.</Text><Text style={{color:c.muted,lineHeight:22}}>Your coach will use your own activity and goals to help you notice patterns and choose a realistic next step.</Text></View></View>
}
const s=StyleSheet.create({page:{flex:1,padding:24,paddingTop:72,gap:12},eyebrow:{fontSize:10,letterSpacing:1.5,fontWeight:"700"},title:{fontSize:30,fontWeight:"700"},card:{padding:22,borderWidth:1,borderRadius:20,marginTop:12},symbol:{fontSize:28,marginBottom:12},cardTitle:{fontSize:18,fontWeight:"700",marginBottom:8}});