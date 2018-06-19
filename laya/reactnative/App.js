import React from 'react';
import {AppRegistry, StyleSheet, Text, View, ScrollView } from 'react-native';
import Apptitle from './src/components/apptitle';
import Volumecontroller from './src/components/volumecontroller';
import Label from './src/components/label';

class LayamApp extends React.Component {
  render() {
    return (
      <ScrollView style={{ paddingTop: 25 }}>
        <View style={{ 
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
        flexDirection: 'row'
        }}>
          <Apptitle />
        </View>

        <View style={styles.container}>
          <Volumecontroller />
          <Label displayText='Mridanga Volume' />
        </View>
        
      </ScrollView>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'flex-start',
    justifyContent: 'flex-start',
    flexDirection: 'column',
    margin: 10
  },
});

AppRegistry.registerComponent('MyReactNativeApp', () => LayamApp);
