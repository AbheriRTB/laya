import React, { Component } from 'react';
import { StyleSheet, Text } from 'react-native';

class Apptitle extends React.Component {
  render() {
    return (
          <Text style={styles.red}>LAYAM METRONOME - APP</Text>
    );
  }
}
const styles = StyleSheet.create({
  bigblue: {
    color: 'blue',
    fontWeight: 'bold',
    fontSize: 60,
  },
  red: {
    color: 'red',
    fontWeight: 'bold',
    textAlign: 'center',
    fontSize: 20,
  },
});
export default Apptitle;