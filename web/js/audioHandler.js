/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// Function to play audio and restore playback position
function playAudio() {
    const audio1 = document.getElementById('backgroundMusic');
    const audio2 = document.getElementById('backgroundMusic2');

    audio2.volume = 0.5;

    // Check if there is a stored time in sessionStorage
    const storedTime1 = sessionStorage.getItem('audioTime1');
    const storedTime2 = sessionStorage.getItem('audioTime2');

    if (storedTime1) {
        audio1.currentTime = storedTime1;
    }
    if (storedTime2) {
        audio2.currentTime = storedTime2;
    }

    // Play audio
    audio1.play();
    audio2.play();

    // Store playback position every second
    setInterval(() => {
        sessionStorage.setItem('audioTime1', audio1.currentTime);
        sessionStorage.setItem('audioTime2', audio2.currentTime);
    }, 1000);
}

// Play the audio when the page loads
window.addEventListener('load', playAudio);

// Function to play error sound
function playErrorSound() {
    const errorSound = new Audio('sound/error.mp3');
    errorSound.play();
}
function playClickSound() {
    const clickSound = new Audio('sound/notification2.mp3');
    clickSound.play();
}