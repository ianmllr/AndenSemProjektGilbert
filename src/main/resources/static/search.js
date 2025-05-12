const searchIcon = document.getElementById('search-icon');
const searchOverlay = document.getElementById('searchOverlay');
const searchInput = document.querySelector('.search-box input');

function isMobile() {
    return window.innerWidth <= 600; // returner true hvis skærmen er mindre eller lige med 600px
}

if (searchIcon) {
    searchIcon.addEventListener('click', (e) => { // lytter til klik på søgeikonet
        e.preventDefault(); // forhindre standardadfærd
        if (isMobile()) {
            searchOverlay.classList.add('show'); // ændre display fra none til flex
            searchInput.focus(); // sætter fokus på søgefeltet, så man automatisk kan skrive
        }
    });
}

if (searchInput) {
    searchInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            searchOverlay.classList.remove('show'); // Lukker overlay

        }
    });
}

if (searchOverlay) {
    searchOverlay.addEventListener('click', (e) => {
        if (e.target === searchOverlay) {
            searchOverlay.classList.remove('show'); // Lukker overlay vis man klikker udenfor det
        }
    });
}