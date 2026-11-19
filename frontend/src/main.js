const catalog = document.querySelector('#catalog');
const status = document.querySelector('#catalog-status');
const cardTemplate = document.querySelector('#anime-card');

function createAnimeCard(anime) {
  const card = cardTemplate.content.cloneNode(true);
  card.querySelector('.card-title').textContent = anime.title ?? 'Без названия';
  card.querySelector('.card-year').textContent = anime.releaseYear ?? '—';
  card.querySelector('.card-episodes').textContent = anime.totalEpisodes ?? '—';
  card.querySelector('.card-description').textContent = anime.description ?? 'Описание отсутствует.';
  return card;
}

async function loadCatalog() {
  try {
    const response = await fetch('/api/anime');
    if (!response.ok) {
      status.textContent = `Не удалось загрузить каталог (HTTP ${response.status}).`;
      return;
    }

    const animeList = await response.json();
    if (!Array.isArray(animeList)) {
      throw new Error('Expected an anime list');
    }

    if (animeList.length === 0) {
      status.textContent = 'Каталог пока пуст.';
      return;
    }

    catalog.replaceChildren(...animeList.map(createAnimeCard));
    status.textContent = '';
  } catch {
    status.textContent = 'Не удалось загрузить каталог. Обновите страницу, чтобы повторить попытку.';
  }
}

loadCatalog();
