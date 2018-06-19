import React, { Component } from 'react';
import { StyleSheet, Text } from 'react-native';

class Label extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    return (
          <Text style={styles.red}>{this.props.displayText}</Text>
    );
  }
}
const styles = StyleSheet.create({
  bigblue: {
    color: 'blue',
    fontWeight: 'bold',
    fontStyle: 'italic',
    fontSize: 15,
  },
  red: {
    color: 'red',
    fontWeight: 'normal',
    fontStyle: 'italic',
    fontSize: 15,
  },
});
export default Label;