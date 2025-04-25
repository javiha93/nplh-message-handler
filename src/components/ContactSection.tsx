
import React from 'react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';
import { Card, CardContent } from '@/components/ui/card';
import { Mail, MapPin, Phone } from 'lucide-react';

const ContactSection: React.FC = () => {
  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    // This would be where you handle the form submission
    console.log('Form submitted');
  };

  return (
    <div className="py-24 gradient-bg">
      <div className="container px-4">
        <div className="text-center mb-16">
          <h2 className="text-3xl md:text-5xl font-bold mb-6 text-white">Say ¡Hola!</h2>
          <p className="text-lg text-white/90 max-w-3xl mx-auto">
            We'd love to hear from you. Reach out and let's start a conversation.
          </p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 max-w-6xl mx-auto">
          <div className="lg:col-span-2">
            <Card className="border-none shadow-2xl">
              <CardContent className="p-6 md:p-8">
                <form onSubmit={handleSubmit} className="space-y-6">
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div className="space-y-2">
                      <label htmlFor="name" className="block text-sm font-medium text-gray-700">
                        Name
                      </label>
                      <Input 
                        id="name" 
                        placeholder="Your name" 
                        className="w-full" 
                        required 
                      />
                    </div>
                    <div className="space-y-2">
                      <label htmlFor="email" className="block text-sm font-medium text-gray-700">
                        Email
                      </label>
                      <Input 
                        id="email" 
                        type="email" 
                        placeholder="your.email@example.com" 
                        className="w-full" 
                        required 
                      />
                    </div>
                  </div>
                  <div className="space-y-2">
                    <label htmlFor="subject" className="block text-sm font-medium text-gray-700">
                      Subject
                    </label>
                    <Input 
                      id="subject" 
                      placeholder="How can we help?" 
                      className="w-full" 
                      required 
                    />
                  </div>
                  <div className="space-y-2">
                    <label htmlFor="message" className="block text-sm font-medium text-gray-700">
                      Message
                    </label>
                    <Textarea 
                      id="message" 
                      placeholder="Your message here..." 
                      className="w-full min-h-[150px]" 
                      required 
                    />
                  </div>
                  <Button type="submit" className="w-full bg-warm-500 hover:bg-warm-600">
                    Send Message
                  </Button>
                </form>
              </CardContent>
            </Card>
          </div>

          <div>
            <Card className="border-none shadow-2xl h-full">
              <CardContent className="p-6 md:p-8 flex flex-col justify-between h-full">
                <div>
                  <h3 className="text-xl font-bold mb-6 text-warm-600">Contact Information</h3>
                  <ul className="space-y-6">
                    <li className="flex items-start">
                      <MapPin className="h-6 w-6 text-warm-500 mt-1 mr-4" />
                      <span className="text-gray-600">
                        123 Sunshine Boulevard<br />
                        Barcelona, Spain 08001
                      </span>
                    </li>
                    <li className="flex items-center">
                      <Phone className="h-6 w-6 text-warm-500 mr-4" />
                      <span className="text-gray-600">+34 123 456 789</span>
                    </li>
                    <li className="flex items-center">
                      <Mail className="h-6 w-6 text-warm-500 mr-4" />
                      <span className="text-gray-600">hola@example.com</span>
                    </li>
                  </ul>
                </div>

                <div className="mt-8 p-4 bg-warm-50 rounded-lg border border-warm-100">
                  <p className="text-gray-700 italic">
                    "Every great relationship starts with a simple hello."
                  </p>
                </div>
              </CardContent>
            </Card>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ContactSection;
