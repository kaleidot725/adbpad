import { defineConfig } from 'vitepress'

export default defineConfig({
  title: 'AdbPad',
  description: 'A GUI tool to streamline Android app testing using ADB',
  base: '/adbpad/',
  head: [
    ['link', { rel: 'icon', href: '/logo.png' }],
  ],
  themeConfig: {
    logo: '/logo.png',
    nav: [
      { text: 'Features', link: '/#features' },
      { text: 'Installation', link: '/#installation' },
      { text: 'Download', link: 'https://github.com/kaleidot725/AdbPad/releases' },
    ],
    socialLinks: [
      { icon: 'github', link: 'https://github.com/kaleidot725/AdbPad' },
    ],
    footer: {
      message: 'Released under the MIT License.',
      copyright: 'Copyright © 2025 Yusuke Katsuragawa',
    },
  },
})
