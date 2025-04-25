
import React from 'react';
import { Button } from '@/components/ui/button';
import { ArrowDown } from 'lucide-react';

const HeroSection: React.FC = () => {
  const scrollToWelcome = () => {
    const welcomeSection = document.getElementById('welcome');
    if (welcomeSection) {
      welcomeSection.scrollIntoView({ behavior: 'smooth' });
    }
  };

  return (
    <div className="relative min-h-screen flex items-center justify-center overflow-hidden">
      <div className="absolute inset-0 z-0">
        <div className="absolute inset-0 bg-gradient-to-br from-warm-50 to-warm-100" />
        <div className="absolute top-0 left-0 w-96 h-96 bg-warm-300 rounded-full filter blur-3xl opacity-20 -translate-x-1/2 -translate-y-1/2" />
        <div className="absolute bottom-0 right-0 w-96 h-96 bg-warm-500 rounded-full filter blur-3xl opacity-20 translate-x-1/2 translate-y-1/2" />
      </div>
      
      <div className="container relative z-10 text-center px-4">
        <h1 className="text-8xl md:text-9xl font-bold mb-8 animate-float">
          <span className="text-gradient">¡Hola!</span>
        </h1>
        <p className="text-xl md:text-2xl max-w-2xl mx-auto text-gray-700 mb-10">
          Welcome to our vibrant and warm digital space where ideas come to life and connections are made
        </p>
        <Button 
          onClick={scrollToWelcome} 
          className="bg-warm-500 hover:bg-warm-600 text-white font-medium px-8 py-6 rounded-full text-lg transition-all duration-300 hover:shadow-lg"
        >
          Discover More <ArrowDown className="ml-2 h-5 w-5" />
        </Button>
      </div>

      <div className="absolute bottom-10 left-0 right-0 flex justify-center animate-bounce">
        <ArrowDown className="h-10 w-10 text-warm-500" />
      </div>
    </div>
  );
};

export default HeroSection;
