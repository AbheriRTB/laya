import React, { Component } from 'react';
import { StyleSheet, Text, Slider, View } from 'react-native';

class Volumecontroller extends React.Component {
  constructor(props) {
    super(props);
    this.state = { volume: 35 };
  }

  render() {
    return (
      <View style={{ 
        flex: 1,
        margin: 0,
        padding: 0,
        borderColor: "orange",
        borderWidth: 0,
        alignItems: 'flex-start',
        justifyContent: 'flex-start',
        flexDirection: 'row'
        }}>
        <Slider
          style={{ width: '100%' }}
          step={1}
          minimumValue={0}
          maximumValue={100}
          value={this.state.volume}
          onValueChange={vol => this.setState({ volume: vol })}
          //onSlidingComplete={volume => this.getVal(volume)}
        />
        <Text style={styles.red}>{this.state.volume}</Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  bigblue: {
    color: 'blue',
    fontWeight: 'bold',
    fontSize: 30,
  },
  red: {
    color: 'red',
  },
});

export default Volumecontroller;
