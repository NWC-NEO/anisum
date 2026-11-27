const app = document.querySelector('#app');
const cardTemplate = document.querySelector('#anime-card');
const detailTemplate = document.querySelector('#anime-detail');

class HttpError extends Error {
  constructor(statusCode) {
    super(`HTTP ${statusCode}`);
    this.statusCode = statusCode;
  }
}

async function getJson(path) {
  const response = await fetch(path);
  if (!response.ok) {
    throw new HttpError(response.status);
  }
  return response.json();
}

function textElement(tag, text, className = '') {
  const element = document.createElement(tag);
  element.textContent = text;
  if (className) element.className = className;
  return element;
}

function showMessage(message, isError = false) {
  const messageElement = textElement('p', message, isError ? 'status-error' : 'status-message');
  messageElement.setAttribute('role', isError ? 'alert' : 'status');
  app.replaceChildren(messageElement);
}

function showError(error, isDetailPage = false) {
  console.error('AniSum API request failed:', error);

  if (isDetailPage && error instanceof HttpError && error.statusCode === 404) {
    showMessage('Аниме не найдено (HTTP 404).', true);
  } else if (error instanceof HttpError) {
    showMessage(`Не удалось загрузить данные (HTTP ${error.statusCode}).`, true);
  } else {
    showMessage('Не удалось загрузить данные. Проверьте соединение и повторите попытку.', true);
  }

  if (isDetailPage) {
    const back = textElement('a', 'Вернуться в каталог', 'back-link');
    back.href = '/';
    app.append(back);
  }
}

function createAnimeCard(anime) {
  const fragment = cardTemplate.content.cloneNode(true);
  const link = fragment.querySelector('.card');
  link.href = `/anime/${encodeURIComponent(String(anime.id))}`;
  fragment.querySelector('.card-title').textContent = anime.title ?? 'Без названия';
  fragment.querySelector('.card-year').textContent = anime.releaseYear ?? '—';
  fragment.querySelector('.card-episodes').textContent = anime.totalEpisodes ?? '—';
  fragment.querySelector('.card-description').textContent = anime.description ?? 'Описание отсутствует.';
  return fragment;
}

async function showCatalog() {
  document.title = 'Каталог аниме | AniSum';
  showMessage('Загрузка каталога…');
  try {
    const animeList = await getJson('/api/anime');
    if (!Array.isArray(animeList)) throw new Error('Expected an array for anime catalog');
    if (animeList.length === 0) {
      showMessage('Каталог пока пуст.');
      return;
    }

    const heading = textElement('h1', 'Каталог аниме');
    const catalog = document.createElement('section');
    catalog.className = 'catalog';
    catalog.setAttribute('aria-label', 'Каталог аниме');
    for (const anime of animeList) catalog.append(createAnimeCard(anime));
    app.replaceChildren(heading, catalog);
  } catch (error) {
    showError(error);
  }
}

function renderAnimeDetail(anime, episodes, relatedParts) {
  const fragment = detailTemplate.content.cloneNode(true);
  fragment.querySelector('.anime-title').textContent = anime.title ?? 'Без названия';
  fragment.querySelector('.anime-meta').textContent =
    `${anime.releaseYear ?? 'Год неизвестен'} · ${anime.totalEpisodes ?? '—'} эпизодов`;
  fragment.querySelector('.anime-description').textContent = anime.description ?? 'Описание отсутствует.';

  const franchiseSection = fragment.querySelector('.franchise');
  const franchiseLinks = fragment.querySelector('.franchise-links');
  const otherParts = relatedParts.filter((part) => String(part.id) !== String(anime.id));
  if (otherParts.length === 0) {
    franchiseSection.remove();
  } else {
    for (const part of otherParts) {
      const item = document.createElement('li');
      const link = textElement('a', part.title ?? 'Без названия');
      link.href = `/anime/${encodeURIComponent(String(part.id))}`;
      item.append(link);
      franchiseLinks.append(item);
    }
  }

  const episodeList = fragment.querySelector('.episode-list');
  const episodeLayout = fragment.querySelector('.episodes-layout');
  const emptyNotice = fragment.querySelector('.episodes-empty');

  if (episodes.length === 0) {
    episodeLayout.remove();
    emptyNotice.hidden = false;
    app.replaceChildren(fragment);
    return;
  }

  const selectedHeading = fragment.querySelector('.episode-title');
  const selectedDescription = fragment.querySelector('.episode-description');
  const previous = fragment.querySelector('.episode-prev');
  const next = fragment.querySelector('.episode-next');
  const episodeButtons = [];
  let selectedIndex = 0;

  function selectEpisode(index) {
    selectedIndex = index;
    const episode = episodes[index];
    selectedHeading.textContent = `${episode.episodeKey ?? '?'} — ${episode.title ?? 'Без названия'}`;
    selectedDescription.textContent = episode.description ?? 'Описание отсутствует.';
    previous.disabled = index === 0;
    next.disabled = index === episodes.length - 1;
    episodeButtons.forEach((button, buttonIndex) => {
      button.setAttribute('aria-pressed', String(index === buttonIndex));
    });
  }

  for (const [index, episode] of episodes.entries()) {
    const item = document.createElement('li');
    const button = textElement('button', `${episode.episodeKey ?? '?'} · ${episode.title ?? 'Без названия'}`);
    button.type = 'button';
    button.className = 'episode-choice';
    button.addEventListener('click', () => selectEpisode(index));
    item.append(button);
    episodeList.append(item);
    episodeButtons.push(button);
  }

  previous.addEventListener('click', () => selectEpisode(selectedIndex - 1));
  next.addEventListener('click', () => selectEpisode(selectedIndex + 1));
  selectEpisode(0);
  app.replaceChildren(fragment);
}

async function showAnimeDetail(animeId) {
  showMessage('Загрузка информации об аниме…');
  try {
    // First check the anime. For a missing ID, avoid unnecessary secondary API calls.
    const anime = await getJson(`/api/anime/${animeId}`);
    const [episodes, relatedParts] = await Promise.all([
      getJson(`/api/anime/${animeId}/episodes`),
      getJson(`/api/anime/${animeId}/franchise-parts`),
    ]);
    if (!Array.isArray(episodes) || !Array.isArray(relatedParts)) {
      throw new Error('Expected arrays for anime episodes and franchise parts');
    }
    document.title = `${anime.title ?? 'Аниме'} | AniSum`;
    renderAnimeDetail(anime, episodes, relatedParts);
  } catch (error) {
    showError(error, true);
  }
}

// Navigation uses normal links. Vite serves index.html for /anime/:id in dev mode.
// A production web server will need an equivalent SPA fallback.
const match = window.location.pathname.match(/^\/anime\/([1-9]\d*)\/?$/);
if (window.location.pathname === '/') {
  showCatalog();
} else if (match) {
  showAnimeDetail(match[1]);
} else {
  showMessage('Страница не найдена.', true);
  const link = textElement('a', 'Вернуться в каталог', 'back-link');
  link.href = '/';
  app.append(link);
}
