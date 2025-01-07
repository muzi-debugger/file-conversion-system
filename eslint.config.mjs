import globals from 'globals';
import prettier from 'eslint-plugin-prettier/recommended';
import tseslint from 'typescript-eslint';
import eslint from '@eslint/js';
import react from 'eslint-plugin-react/configs/recommended.js';
import cypress from 'eslint-plugin-cypress/flat';

// jhipster-needle-eslint-add-import - JHipster will add additional import here

export default {
  languageOptions: {
    globals: {
      ...globals.node,
    },
    parserOptions: {
      ecmaVersion: 2021,
    },
  },
  extends: [
    'eslint:recommended', // Use string 'eslint:recommended' for ESLint's recommended rules
    'plugin:react/recommended',
  ],
  rules: {
    'no-unused-vars': ['error', { argsIgnorePattern: '^_' }],
    'constructor-super': 'off', // Disable constructor-super rule
  },
  overrides: [
    {
      files: ['**/*.{js,cjs,mjs}'],
      rules: {
        'constructor-super': 'off', // Disable constructor-super rule
      },
    },
    {
      files: ['src/main/webapp/**/*.{ts,tsx}'],
      extends: [...tseslint.configs.recommendedTypeChecked, react],
      settings: {
        react: {
          version: 'detect',
        },
      },
      languageOptions: {
        globals: {
          ...globals.browser,
        },
        parserOptions: {
          project: ['./tsconfig.json', './tsconfig.test.json'],
        },
      },
      rules: {
        // Add specific TypeScript rules here
      },
    },
    {
      files: ['src/main/webapp/**/*.spec.ts'],
      rules: {
        '@typescript-eslint/no-empty-function': 'off',
      },
    },
    {
      files: ['src/test/javascript/cypress/**/*.ts'],
      extends: [...tseslint.configs.recommendedTypeChecked, cypress.configs.recommended],
      languageOptions: {
        parserOptions: {
          project: ['./src/test/javascript/cypress/tsconfig.json'],
        },
      },
      rules: {
        '@typescript-eslint/no-explicit-any': 'off',
        '@typescript-eslint/no-unsafe-argument': 'off',
        '@typescript-eslint/no-unsafe-assignment': 'off',
      },
    },
  ],
  // jhipster-needle-eslint-add-config - JHipster will add additional config here
  ...prettier,
};
