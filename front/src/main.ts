// ... existing code ...
import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import { createPinia } from 'pinia';
import './styles/_variables.scss';

const app = createApp(App);

app.use(createPinia());
app.use(router);
app.mount('#app');
// ... existing code ...