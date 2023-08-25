/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// Conf ALV ********************************
console.log("Device: " + window.screen.width);
var scrollStatus = false;
if(window.screen.width <= 375) {
    scrollStatus = true;
}

////Local DEV//
var link = "http://localhost:8080/doctilas/";